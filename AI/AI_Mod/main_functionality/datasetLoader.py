import os
import pandas as pd
import chromadb
from langchain_community.embeddings import OpenAIEmbeddings
api = os.getenv("OPENAI_API_KEY")
csv_file_path = "../dataset/combined_dataset.csv"
data = pd.read_csv(csv_file_path)
context = data["Context"].tolist()
responses = data["Response"].tolist()
embedding_model = OpenAIEmbeddings(openai_api_key= api)
embedded_context = [embedding_model.embed_query(text) for text in context]
chroma_client = chromadb.PersistentClient(path="./vector_DB")
collection_name = "embedded_context"
collection = chroma_client.get_or_create_collection(name=collection_name)

MAX_BATCH_SIZE = 5461

for batch_start in range(0, len(context), MAX_BATCH_SIZE):
    batch_ids = [str(i) for i in range(batch_start, min(batch_start + MAX_BATCH_SIZE, len(context)))]
    context_batch = context[batch_start:batch_start + MAX_BATCH_SIZE]
    embedded_context_batch = embedded_context[batch_start:batch_start + MAX_BATCH_SIZE]
    response_batch = [
        {"response": responses[i]}
        for i in range(batch_start, batch_start + len(context_batch))
    ]
    collection.add(
        ids=batch_ids,
        documents=context_batch,
        metadatas=response_batch,
        embeddings=embedded_context_batch
    )
