# API Challenge

## Dependencies

This project uses Maven for builds.
You need Java 8 installed.

## Building

```
$ mvn package
```

## Running

```
$ java -jar target/api_interview-0.1.0.jar
```

## API

```
GET /dogs?clientID=xyz

returns

[
  'breed': ...
  'dogs': [
    {'id': ...   string
    'breed': ... string
    'votes": ... number
    'haveVoted': [true/false]
    'url': ...}, string
  ]
]

GET /dogs/:breed?clientID=xyz

returns

[
  {'id': ...
  'breed': ...
  'votes": ...
  'haveVoted': ...
  'url': ...},
]

POST /dog/:id?clientID=xyz

body: {action: 'up'}
```
