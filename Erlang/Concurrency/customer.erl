-module(customer).
-export([makeCustomerThreads/1]).

makeCustomerThreads(CustomersList) ->
    CustomersList.