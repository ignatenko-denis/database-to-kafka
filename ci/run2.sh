#!/bin/sh
app_path=$(find ../target -type f -name "*.jar")
java -jar $app_path --server.port=8002
