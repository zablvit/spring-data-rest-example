#!/bin/bash

# ------------------ Create continents

curl --request POST \
  --url http://localhost:8080/continents \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"name":"Australia"}'

curl --request POST \
  --url http://localhost:8080/continents \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"name":"North America"}'

# ------------------ Create countries

curl --request POST \
  --url http://localhost:8080/countries \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"name":"USA", "continent":{"name":"North America"}}'

curl --request POST \
  --url http://localhost:8080/countries \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"name":"Canada", "continent":{"name":"North America"}}'

curl --request POST \
  --url http://localhost:8080/countries \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"name":"New Zealand", "continent":{"name":"Australia"}}'

# ------------------ Create Cities

curl --request POST \
  --url http://localhost:8080/cities \
  --header 'content-type: application/json' \
  --header 'accept: application/json' \
  --data '{"name":"New York","country":{"name":"USA"}}'

curl --request POST \
  --url http://localhost:8080/cities \
  --header 'content-type: application/json' \
  --header 'accept: application/json' \
  --data '{"name":"Los Angeles","country":{"name":"USA"}}'

curl --request POST \
  --url http://localhost:8080/cities \
  --header 'content-type: application/json' \
  --header 'accept: application/json' \
  --data '{"name":"Ottawa","country":{"name":"Canada"}}'

curl --request POST \
  --url http://localhost:8080/cities \
  --header 'content-type: application/json' \
  --header 'accept: application/json' \
  --data '{"name":"Quebec","country":{"name":"Canada"}}'

curl --request POST \
  --url http://localhost:8080/cities \
  --header 'content-type: application/json' \
  --header 'accept: application/json' \
  --data '{"name":"Wellington","country":{"name":"New Zealand"}}'