-module(money).
-export([start/0, initialPrints/0, start_recv/0]).

initialPrints() ->
    {ok, BankList} = file:consult("banks.txt"),
    Banks = maps:from_list(BankList),
    {ok, CustomerList} = file:consult("customers.txt"),
    Customers = maps:from_list(CustomerList),
    io:fwrite("*** Customers and loan objectives ***\n"),
    maps:fold(fun(K, V, ok) -> io:format("~p: ~p~n", [K, V]) end, ok, Customers),
    io:fwrite("*** Banks and financial resources ***\n"),
    maps:fold(fun(K, V, ok) -> io:format("~p: ~p~n", [K, V]) end, ok, Banks).

start_recv() ->
    receive
        {Customer, yes, Amount, Bank, Balance} ->
            io:fwrite("~p approved a loan of ~p from ~p ~p\n",[Bank, Amount, Customer, Balance]),
            start_recv();
        {Customer, no, Amount, Bank, Balance} ->
            io:fwrite("~p rejected a loan of ~p from ~p ~p\n",[Bank, Amount, Customer, Balance]),
            start_recv();
        {Customer, request, Amount, Bank} ->
            io:fwrite("~p requested a loan of ~p from ~p\n",[Customer, Amount, Bank]),
            start_recv();
        {BankName, closing, Balance} ->
            io:fwrite("~p Closed with Balance: ~p", BankName, Balance),
            start_recv()
    end
.

start() ->
    initialPrints(),
    {ok, BankList} = file:consult("banks.txt"),
    {ok, CustomerList} = file:consult("customers.txt"),
    Banks = maps:from_list(BankList),
    bank:makeBankProcesses(BankList),
    register(master_recv, spawn(?MODULE, start_recv, [])),
    Blist = maps:keys(Banks),
    io:fwrite("\nLet the begging begin!\n\n"),
    customer:makeCustomerProcesses(CustomerList, Blist)
.