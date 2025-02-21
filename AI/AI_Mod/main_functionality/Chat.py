import time
import os
import speech_recognition as sr
from google.oauth2 import service_account
from langchain.chat_models import ChatOpenAI
from langchain.schema import HumanMessage
from langchain_community.embeddings import OpenAIEmbeddings
from flask import Flask, request, send_file, jsonify
from deep_translator import GoogleTranslator
from google.cloud import texttospeech
import chromadb
api = os.getenv("OPENAI_API_KEY")
app = Flask(__name__)
llm = ChatOpenAI(model_name="gpt-4o",openai_api_key= api)
service_account_file = r'C:\Users\osama\Desktop\credentials\realtimeconversation-e31fc3fa3a0a.json'
credentials = service_account.Credentials.from_service_account_file(service_account_file)
tts_client = texttospeech.TextToSpeechClient(credentials=credentials)
chroma_client = chromadb.PersistentClient(path="./vector_DB")
embedding_model = OpenAIEmbeddings(openai_api_key= api)
chat_history = []


def retrieve_response(query):
    query_embedding = embedding_model.embed_query(query)
    collection_name = "embedded_context"
    collection = chroma_client.get_or_create_collection(name=collection_name)
    results = collection.query(
        query_embeddings=[query_embedding],
        n_results=1
    )
    print(results)
    return results


def llm_response(user_query):
    retrieved_response = retrieve_response(user_query)
    prompt = f"""
    Chat history: "{chat_history}"
    User: "{user_query}"
    conversation examples: "{retrieved_response}"
    Instructions: your name is BotSoul, you are mental health chatbot, be kind to the user and provide a helpful and accurate response for user input take conversation examples as guides for you and make the chat more engaging, interesting, and real, use chat history to refine the response and make it more relevant to the user.
    """
    refined_response = llm([HumanMessage(content=prompt)])
    return refined_response.content


def tts(text):
    language_code = "ar-XA"
    timestamp = int(time.time())
    audio_file_path = f"./response/output_{timestamp}.wav"
    synthesis_input = texttospeech.SynthesisInput(text=text)
    voice = texttospeech.VoiceSelectionParams(
        language_code=language_code,
        ssml_gender=texttospeech.SsmlVoiceGender.NEUTRAL
    )
    audio_config = texttospeech.AudioConfig(
        audio_encoding=texttospeech.AudioEncoding.LINEAR16
    )
    response = tts_client.synthesize_speech(
        input=synthesis_input, voice=voice, audio_config=audio_config
    )
    with open(audio_file_path, "wb") as out:
        out.write(response.audio_content)
        return audio_file_path


def stt(audio):
    recognizer = sr.Recognizer()
    temp_filename = "./context/context.wav"
    try:
        with sr.AudioFile(temp_filename) as source:
            audio_data = recognizer.record(source)
            text = recognizer.recognize_google(audio_data, language='ar-SA')
            return text
    except sr.UnknownValueError:
        return "Sorry, I did not understand that."
    except sr.RequestError:
        return "Sorry, my speech service is down."
    finally:
        if os.path.exists(temp_filename):
            os.remove(temp_filename)


def translate_to_arabic(text):
    return GoogleTranslator(source="en", target="ar").translate(text)


def translate_to_english(text):   
    return GoogleTranslator(source="ar", target="en").translate(text)

@app.route("/send_receive_audio", methods=["POST"])
def send_receive_audio():
    if "audio" not in request.files:
        return jsonify({"error": "Missing 'audio' file"}), 400
    audio_file = request.files["audio"]
    # Save the incoming audio file to the 'context' directory
    audio_directory = "./context"
    os.makedirs(audio_directory, exist_ok=True)
    audio_file_path = os.path.join(audio_directory, audio_file.filename)
    audio_file.save(audio_file_path)

    audio_data = audio_file.read()
    user_query = stt(audio_data)
    if not user_query:
        return jsonify({"error": "Could not transcribe audio"}), 400
    chat_history.append(user_query)
    response = llm_response(translate_to_english(user_query))
    outfile = tts(translate_to_arabic(response))
    return send_file(outfile, mimetype="audio/wav")


@app.route("/send_receive_text", methods=["POST"])
def send_receive_text():
    if "text" not in request.form:
        return jsonify({"error": "Missing 'text' field"}), 400
    user_query = request.form["text"]
    chat_history.append(translate_to_english(user_query))
    response = llm_response(translate_to_english(user_query))
    return jsonify({"response": translate_to_arabic(response)})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
