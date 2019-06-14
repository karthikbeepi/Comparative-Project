-module(money).
-export([start/0]).
-import(bank,[makeBankThreads/1]).
-import(customer,[makeCustomerThreads/1]).

start() ->
        {ok, BankList} = file:consult("banks.txt"),
        Banks = maps:from_list(BankList),
        {ok, CustomerList} = file:consult("customers.txt"),
        Customers = maps:from_list(CustomerList),
        % B = makeBankThreads(Banks),
        % C = makeCustomerThreads(Customers),
        io:fwrite("~p : ~p\n", [makeBankThreads(Banks), Customers]).