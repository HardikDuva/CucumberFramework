@ECHO OFF
TITLE GRID Firefox Node

java -jar jar/selenium-server-4.6.0.jar node --port 32402 --config config/firefoxConfig.toml --log "./log//FirefoxNodeLogFile.txt" --log-encoding "UTF-8"

