OS := $(shell uname -s)

all:
	mvn clean package clean

ifeq ($(OS),Linux)
	java -jar xmashl00.jar

else ifeq ($(OS),Darwin)
	java -Xdock:icon=./lib/robot.jpg -jar xmashl00.jar

else ifeq ($(OS),Windows_NT)
	java -jar xmashl00.jar

else
	@echo "Unknown operating system."
endif

.PHONY: all
