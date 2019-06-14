-module(money).
-export([start/0, initialPrints/0]).
-import(bank,[makeBankThreads/1]).
-import(customer,[makeCustomerThreads/1]).

initialPrints() ->
    {ok, BankList} = file:consult("banks.txt"),
    Banks = maps:from_list(BankList),
    {ok, CustomerList} = file:consult("customers.txt"),
    Customers = maps:from_list(CustomerList),
    io:fwrite("*** Customers and loan objectives ***\n"),
    maps:fold(fun(K, V, ok) -> io:format("~p: ~p~n", [K, V]) end, ok, Customers),
    io:fwrite("*** Banks and financial resources ***\n"),
    maps:fold(fun(K, V, ok) -> io:format("~p: ~p~n", [K, V]) end, ok, Banks).

start() ->
    initialPrints().