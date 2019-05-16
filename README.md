# credit-calculator
This is a repo for Credits Calculator Test App deployment

# Deployment

## Defaults
Default servlet name is "credits".

## Database seeds
It's required to seed inta database two Role entities with Name *"Admin"* and *"User"*.

## Requirements
Node.js should be installed to be able to build the frontend.
Gradle should be installed to build a WAR

## Prepare WAR artifact

If you want to have servlet name "credits", skip p. 1-3.

To get the ready for deploy on the Tomcat server WAR artifact, you should complete some steps:
1. Open *bsuir.creditcalculator.application/config/config.json* file and replace "baseUrl" value with the desired WAR artifact name.
2. Go to the *bsuir.creditcalculator.application* folder and launch the build action:
    ```
    cd bsuir.creditcalculator.application
    npm install --save
    npm run build-prod
    ```
3. Copy *bsuir.creditcalculator.application/dist/bundle.js* and *bsuir.creditcalculator.application/dist/fonts* to *bsuir.creditcalculator/bsuir.credit-calculator/bsuir.credit-calculator.web/src/main/webapp/resources*
4. Adjust database connection properties in the *bsuir.creditcalculator/bsuir.credit-calculator/bsuir.credit-calculator.web/src/main/webapp/WEB-INF/classes/hibernate.properties*:
    - hibernate.connection.url
    - hibernate.connection.username
    - hibernate.connection.password
4. Go to the *bsuir.creditcalculator* folder and build the WAR:
    ```
    cd bsuir.creditcalculator
    gradle war
    ```
    The WAR artifact will be generated at the *bsuir.creditcalculator/bsuir.credit-calculator/bsuir.credit-calculator.web/build/libs/bsuir.credit-calculator*
5. Rename the artifact to name you specified when configured frontend.

The artifact is ready to deploy.