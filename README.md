# vertx-microsrv-poc
This is yet another study project

# Implementation notes
## Multiple instances of a listing Verticle 
When deploying multiple time a Vertice listing to a TCP socket (ie. more than 1 instances), vert.x will implement a (non-strict) round robin among your verticles.  
https://groups.google.com/d/msg/vertx/IzyurInyaRE/InYHPs3UBwAJ