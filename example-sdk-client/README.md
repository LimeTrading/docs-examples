# Java Example Client

This project is to demonstrate the usage of the Lime Trading SDK for java. 

Main class can be found [here](./app/src/main/java/client/App.java)


## Usage

This project uses [Gradle](https://gradle.org/) and Java 19.

```
$ ./gradlew run --args="--help" --console=plain

> Task :app:compileJava UP-TO-DATE
> Task :app:processResources NO-SOURCE
> Task :app:classes UP-TO-DATE

> Task :app:run FAILED
usage: example
 -a,--account <arg>    account to login with
 -H,--host <arg>       host ip
 -P,--port <arg>       port to connect to
 -p,--password <arg>   password
 -u,--username <arg>   username
2 actionable tasks: 1 executed, 1 up-to-date
```

To run the demo program:

```
./gradlew run --args="--host [HOST NAME] --port [PORT] --account [YOUR TRADING ACCOUNT NAME] --username [USERNAME] --password [PASSWORD] " --console=plain
```

Command list:

```
Help:
--------------
place: place a new order
place us option: place a new us option
place algo: place a new algo order
place us options algo: place a new us options algo order
replace: replace existing order
replace us options: replace existing us option
replace algo: replace existing algo order
replace us options algo: replace existing us option algo order
cancel: cancel an order
partial cancel: partially cancel an order
cancel all: cancel all orders for this account
```

## Troubleshooting

### `NotConnectedException`

* Make sure your trading account is disconnected before starting the demo program
