# Showcase of MongoDB transaction problem in a concurrent environment

In a situation where multiple threads are trying to update the same document
`WriteConflict` exception can happen.

Here is presented two different ways how to handle transactions.

## Failing transaction

Details can be checked in the `FailedBookService` class. It is only used `@Transactional`
annotation which is a very elegant solution, but it will not save us from the `WriteConflict` exception.

## Successful transaction

Details can be found in `SuccessfulBookService`. It uses a `mongoTemplate` and mongo `ClientSession` to handle the
transaction. Retry mechanism is added to deal with concurrency.

## How to test it?

To run the all environment you'll need a Docker installed. To simulate heavy traffic I've used a JMeter app.

Perform the following steps:

1. From the root of the project run the following command `./infra/mongo-replica.sh`. This will start up a MongoDB
   instance for you in ReplicaSet mode.
2. Run the application using a profile `failed` or `successful`.
3. Download and run JMeter.
4. Open in JMeter `transaction-test.jmx` file which you can find here in the project.
5. Hit the Start button to run the simulation.

While testing with the `failed` profile you should find a lot of failed requests in JMeter. On the other hand, while
testing with the `successful` profile all requests will make it, however, some delays are noticeable.
