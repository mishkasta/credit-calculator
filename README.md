# credit-calculator
This is a repo for Credits Calculator Test App deployment

# Deployment

## Defaults
Default servlet name should be "credits".

## Requirements
Node.js and yarn should be installed to be able to build the frontend.

To get the ready for deploy on the Tomcat server WAR artifact, you should complete some steps:
1. Open bsuir.creditcalculator.application/config/config.json file and replace "baseUrl" value with the desired WAR artifact name.
2. Go to the bsuir.creditcalculator.application folder and launch the build action:
  ```
  cd bsuir.creditcalculator.application
  yarn build-prod
  ```