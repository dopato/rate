# RateApp
Prerequisites:
- Java SE Development Kit 8

Build
./gradlew clean build
Check the logback.xml if wish to change logging level.
The new jar will be in the  build/libs folder.

Run the application:
java -jar rate.jar [market_file_path] [loan_amount]

The gradle application plugin has been applied and is possible to run the application also using: 
./gradlew run --args="[market_file_path] [loan_amount]"
e.g.
./gradlew run --args="src/test/resources/market.csv 1000"
