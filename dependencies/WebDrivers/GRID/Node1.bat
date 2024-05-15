@ECHO OFF
TITLE GRID Chrome Node

java -jar jar/selenium-server-4.6.0.jar node --port 32401 --config config/chromeConfig.toml --log "./log//ChromeNodeLogFile.txt" --log-encoding "UTF-8"