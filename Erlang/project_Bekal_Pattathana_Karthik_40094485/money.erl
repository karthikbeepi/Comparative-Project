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
            io:fwrite("~p approved a loan of ~p from ~p \n",[Bank, Amount, Customer]),
            start_recv();
        {Customer, no, Amount, Bank, Balance} ->
            io:fwrite("~p denies a loan of ~p from ~p \n",[Bank, Amount, Customer]),
            start_recv();
        {Customer, request, Amount, Bank} ->
            io:fwrite("~p requested a loan of ~p from ~p\n",[Customer, Amount, Bank]),
            start_recv();
        {closing, BankName,  Balance} ->
            io:fwrite("~p has ~p dollar(s) remaining.\n", [BankName, Balance]),
            start_recv();
        {CustomerName, done, LoanAmt} ->
            io:fwrite("\n~p has reached the objective of ~p dollar(s). Woo Hoo!\n", [CustomerName, LoanAmt]),
            start_recv();
        {CustomerName, notdone, LoanAmt} ->
            io:fwrite("\n~p was only able to borrow ~p dollar(s). Boo Hoo!\n", [CustomerName, LoanAmt]),
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
    io:fwrite("\n"),
    customer:makeCustomerProcesses(CustomerList, Blist),
    timer:sleep(1500)
.