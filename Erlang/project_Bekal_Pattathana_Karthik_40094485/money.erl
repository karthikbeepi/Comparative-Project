-module(money).
-export([start/0, initialPrints/0, start_recv/0, makeCustomerProcesses/2, makeBankProcesses/1]).

initialPrints() ->
    {ok, BankList} = file:consult("banks.txt"),
    Banks = maps:from_list(BankList),
    {ok, CustomerList} = file:consult("customers.txt"),
    Customers = maps:from_list(CustomerList),
    io:fwrite("******* Customers and loan objectives *******\n"),
    maps:fold(fun(K, V, ok) -> io:format("~p: ~p~n", [K, V]) end, ok, Customers),
    io:fwrite("******* Banks and financial resources *******\n"),
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
        {CustomerName, notdone, LoanAmt} ->
            io:fwrite("\n~p was only able to borrow ~p dollar(s). Boo Hoo!\n", [CustomerName, LoanAmt]),
            start_recv();
        {CustomerName, done, LoanAmt} ->
            io:fwrite("\n~p has reached the objective of ~p dollar(s). Woo Hoo!\n", [CustomerName, LoanAmt]),
            start_recv()
after 2000 -> ok
    end
    % main_receiver_exiting
.

makeCustomerProcesses([], BankList) ->
ok;
makeCustomerProcesses([{K , V}|Rest], BankList) ->
    register(K, spawn(customer, customer, [K, V, BankList, [], 0])),
    makeCustomerProcesses(Rest, BankList).

makeBankProcesses([]) ->
    ok;
makeBankProcesses([{K , V}|Rest]) ->
    register(K, spawn(bank, bank, [K, V])),
    makeBankProcesses(Rest).

start() ->
    initialPrints(),
    {ok, BankList} = file:consult("banks.txt"),
    {ok, CustomerList} = file:consult("customers.txt"),
    Banks = maps:from_list(BankList),
    makeBankProcesses(BankList),
    register(master_recv, self()),
    Blist = maps:keys(Banks),
    io:fwrite("\n"),
    makeCustomerProcesses(CustomerList, Blist),
    start_recv(),
    main_program_exiting
.