# Spring Data Rest example

### Table of Contents

* [Features](#features)
* [API](#api)
* [Installation](#installation)
* [Import data](#import-data)
* [Usage Examples](#usage-examples)
    + [Create Continent](#create-continent)
    + [List Continents](#list-continents)
    + [Show single Continent](#show-single-continent)
    + [Create Country](#create-country)
    + [List Countries](#list-countries)
    + [Show single Country](#show-single-country)
    + [Create City](#create-city)
    + [List Cities](#list-cities)
    + [Show single City](#show-single-city)
    + [List Countries in Continent](#list-countries-in-continent)
    + [List Cities in Country](#list-cities-in-country)
    + [List Cities in Continent](#list-cities-in-continent)

## Features

* HAL
* Search queries
* Client friendly API
* Web UI friendly API

## API

| Endpoint           | Methods             |
| ------------------ | ------------------- |
| /                  | GET                 |
| /continents        | GET, POST           |
| /continents/{id}   | GET, DELETE         |
| /countries         | GET, POST           |
| /countries/{id}    | GET, DELETE         |
| /cities            | GET, POST           |
| /cities/{id}       | GET, DELETE, PUT    |

## Installation

1. Compile and create docker image `map-app:latest`

    ```bash
    mvn clean package dockerfile:build
    ```

2. Run docker compose

    ```bash
    docker-compose up
    ```

## Import data

You can populate application with some sample data by running `init-data.sh` script

> NOTICE: You need curl utility to be present on your system for  this to work



```bash
./init-data.sh
```

## Usage Examples

### Create Continent

**Request**

```bash
curl --request POST \
  --url http://localhost:8080/continents \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"name":"Africa"}'
```

**Response**

```
HTTP Status 201 Created
```

### List Continents

**Request**

```bash
curl --request GET \
  --url http://localhost:8080/continents \
  --header 'accept: application/json'
```

**Response**

```json
{
  "_embedded" : {
    "continents" : [ {
      "name" : "Africa",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/continents/5af30e7fbe077700018d3960"
        },
        "continent" : {
          "href" : "http://localhost:8080/continents/5af30e7fbe077700018d3960"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/continents{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/continents"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 1,
    "totalPages" : 1,
    "number" : 0
  }
}
```

### Show single Continent

**Request**

```bash
curl --request GET \
  --url http://localhost:8080/continents/5af30bedbe077700018d395e \
  --header 'accept: application/json'
```

**Response**

```json
{
  "name" : "Africa",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/continents/5af30e7fbe077700018d3960"
    },
    "continent" : {
      "href" : "http://localhost:8080/continents/5af30e7fbe077700018d3960"
    }
  }
}
```

### Create Country

**Request**

```bash
curl --request POST \
  --url http://localhost:8080/countries \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"name":"Egypt", "continent":{"name":"Africa"}}'
```

**Response**

```
HTTP Status 201 Created
```

### List Countries

**Request**

```bash
curl --request GET \
  --url http://localhost:8080/countries \
  --header 'accept: application/json'
```

**Response**

```json
{
  "_embedded" : {
    "countries" : [ {
      "name" : "Egypt",
      "continent" : {
        "name" : "Africa"
      },
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/countries/5af3118abe077700018d3961"
        },
        "country" : {
          "href" : "http://localhost:8080/countries/5af3118abe077700018d3961"
        },
        "continent" : {
          "href" : "http://localhost:8080/continents/5af30e7fbe077700018d3960"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/countries{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/countries"
    },
    "search" : {
      "href" : "http://localhost:8080/countries/search"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 1,
    "totalPages" : 1,
    "number" : 0
  }
}
```


### Show single Country

**Request**

```bash
curl --request GET \
  --url http://localhost:8080/countries/5af3118abe077700018d3961 \
  --header 'accept: application/json' \
  --header 'cache-control: no-cache' \
  --header 'postman-token: ae52ea3a-f88c-3ca8-9806-93a2161621f6'
```


**Response**

```json
{
    "name": "Egypt",
    "continent": {
        "name": "Africa"
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/countries/5af3118abe077700018d3961"
        },
        "country": {
            "href": "http://localhost:8080/countries/5af3118abe077700018d3961"
        },
        "continent": {
            "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
        }
    }
}
```

### Create City

**Request**

```bash
curl --request POST \
  --url http://localhost:8080/cities \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data '{"name":"Cairo","country":{"name":"Egypt"}}'
```

**Response**

```
HTTP Status 201 Created
```

### List Cities

**Request**
```bash
curl --request GET \
  --url http://localhost:8080/cities \
  --header 'accept: application/json'
```

**Response**

```json
{
    "_embedded": {
        "cities": [
            {
                "name": "Cairo",
                "country": {
                    "name": "Egypt",
                    "continent": {
                        "name": "Africa"
                    },
                    "_links": {
                        "continent": {
                            "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
                        }
                    }
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/cities/5af43c1f410df80001d7ed51"
                    },
                    "city": {
                        "href": "http://localhost:8080/cities/5af43c1f410df80001d7ed51"
                    },
                    "country": {
                        "href": "http://localhost:8080/countries/5af3118abe077700018d3961"
                    },
                    "continent": {
                        "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/cities{?page,size,sort}",
            "templated": true
        },
        "profile": {
            "href": "http://localhost:8080/profile/cities"
        },
        "search": {
            "href": "http://localhost:8080/cities/search"
        }
    },
    "page": {
        "size": 20,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}
```

### Show single City

**Request**

```bash
curl --request GET \
  --url http://localhost:8080/cities/5af43c1f410df80001d7ed51 \
  --header 'accept: application/json'
```

**Response**

```json
{
    "name": "Cairo",
    "country": {
        "name": "Egypt",
        "continent": {
            "name": "Africa"
        },
        "_links": {
            "continent": {
                "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
            }
        }
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/cities/5af43c1f410df80001d7ed51"
        },
        "city": {
            "href": "http://localhost:8080/cities/5af43c1f410df80001d7ed51"
        },
        "country": {
            "href": "http://localhost:8080/countries/5af3118abe077700018d3961"
        },
        "continent": {
            "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
        }
    }
}
```

### List Countries in Continent

**Request**

```bash
curl --request GET \
  --url 'http://localhost:8080/countries/search/byContinentName?continentName=Africa' \
  --header 'accept: application/json'
```

**Response**

```json
{
    "_embedded": {
        "countries": [
            {
                "name": "Egypt",
                "continent": {
                    "name": "Africa"
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/countries/5af3118abe077700018d3961"
                    },
                    "country": {
                        "href": "http://localhost:8080/countries/5af3118abe077700018d3961"
                    },
                    "continent": {
                        "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/countries/search/byContinentName?continentName=Africa&page=0&size=20"
        }
    },
    "page": {
        "size": 20,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}
```

### List Cities in Country

**Request**

```bash
curl --request GET \
  --url 'http://localhost:8080/cities/search/byCountryName?countryName=Egypt' \
  --header 'accept: application/json'

```

**Response**

```json
{
    "_embedded": {
        "cities": [
            {
                "name": "Cairo",
                "country": {
                    "name": "Egypt",
                    "continent": {
                        "name": "Africa"
                    },
                    "_links": {
                        "continent": {
                            "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
                        }
                    }
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/cities/5af43c1f410df80001d7ed51"
                    },
                    "city": {
                        "href": "http://localhost:8080/cities/5af43c1f410df80001d7ed51"
                    },
                    "country": {
                        "href": "http://localhost:8080/countries/5af3118abe077700018d3961"
                    },
                    "continent": {
                        "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/cities/search/byCountryName?countryName=Egypt&page=0&size=20"
        }
    },
    "page": {
        "size": 20,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}
```

### List Cities in Continent

**Request**

```bash
curl --request GET \
  --url 'http://localhost:8080/cities/search/byContinentName?continentName=Africa' \
  --header 'accept: application/json'
```

**Response**

```json
{
    "_embedded": {
        "cities": [
            {
                "name": "Cairo",
                "country": {
                    "name": "Egypt",
                    "continent": {
                        "name": "Africa"
                    },
                    "_links": {
                        "continent": {
                            "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
                        }
                    }
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/cities/5af43c1f410df80001d7ed51"
                    },
                    "city": {
                        "href": "http://localhost:8080/cities/5af43c1f410df80001d7ed51"
                    },
                    "country": {
                        "href": "http://localhost:8080/countries/5af3118abe077700018d3961"
                    },
                    "continent": {
                        "href": "http://localhost:8080/continents/5af30e7fbe077700018d3960"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/cities/search/byContinentName?continentName=Africa&page=0&size=20"
        }
    },
    "page": {
        "size": 20,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}
```