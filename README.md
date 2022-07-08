# Challenge

## :computer: How to execute
# Used following tech stack in this project
   * Java 11
   * Maven >= 3.3.9
   * Spring boot 2.6.9
   * Mockito
   * Lombok 
 
All the changes for Payment Service to run as Docker container has been done and updated in docker compose yaml.
To start the services run `docker-compose up -d`
## :memo: Notes

* Spring Kafka is used to create the consumer and listen the data published into the topics `online` and `offline`
* Feign client is used to communicate with API service for `Payment Validation` and `Logging`

## :pushpin: Things to improve

* Create multiple partitions to improve the parallel processing and performance 
* Mount volumes for kafka server, Zookeeper and Postgress to avoid loss of data
* 
