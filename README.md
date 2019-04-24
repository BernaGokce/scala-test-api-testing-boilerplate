# Bookstore

## Table of Contents

1. [Introduction](#introduction)
2. [Development](#development)
3. [Testing ](#testing)
4. [Contributing](#contributing)
5. [License](#license)

## Introduction

ScalaTraning is a boiler plate for backend api testing with Scala Test. It uses Bookstore backend application which is also written in Scala. It is a dummy REST API for authors and books.

ScalaTraning's main purpose is to create architecture for api testing.

You can find bookstore application from this link (<https://github.com/makiftutuncu/bookstore>)

To run book store application.

1- You should install SBT.

```brew install sbt```

2- You should download docker.

3- In order to get the database set up,

```docker-compose up -d```

4- On the project path,

```sbt run```

## Development and Running

ScalaTraning is built with SBT. So, standard SBT tasks like `clean`, `compile`, `test` and `testOnly` can be used.

To run all the tests, use `test` task of SBT.

To run specific test(s), use

```testOnly com.apitest.BookStoreAuthorSpec```

or

```testOnly com.apitest.BookStoreBookSpec```

## Testing with different envs and services

To run specific env and service,

```ENV=stg SERVICE=core sbt```

If you did not give any configuration, it use "Local" env and "Core" services.

And to see report file, "target > test-reports > index.html"

## Contributing

Perhaps there is not much to develop here but all contributions are more than welcome. Please feel free to send a pull request for your contributions. Thank you.

## License

Bookstore is licensed with MIT License. See [LICENSE.md](LICENSE.md) for details.

