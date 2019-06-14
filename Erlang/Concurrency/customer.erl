-module(customer).
-export([makeCustomerProcesses/2, customer/4 , getRandomBank/1]).

getRandomBank(Blist) ->
        BBlist = lists:nth(rand:uniform(3), Blist),
        BBlist
.
customer(CustomerName, LoanAmt, BankList, NotToCall) when LoanAmt =< 0 ->
    ok;
customer(CustomerName, LoanAmt, BankList, NotToCall) when LoanAmt > 0->
    RandomBank = getRandomBank(BankList),
    RandomAmt = rand:uniform(50),
    if
        (RandomAmt >= LoanAmt) ->
            RandomBank ! {CustomerName, LoanAmt},
            master_recv ! {CustomerName, request, LoanAmt, RandomBank};
        true ->
            RandomBank ! {CustomerName, RandomAmt},
            master_recv ! {CustomerName, request, RandomAmt, RandomBank}
    end,
    receive
        {yes} ->
            customer(CustomerName, LoanAmt-RandomAmt, BankList, NotToCall);
        {no} ->
            % NewList = lists:append(NotToCall, RandomBank),
            NewList = [RandomBank | NotToCall],
            customer(CustomerName, LoanAmt, BankList, NewList)
    end

    .

makeCustomerProcesses([], BankList) ->
ok;
makeCustomerProcesses([{K , V}|Rest], BankList) ->
    register(K, spawn(?MODULE, customer, [K, V, BankList, []])),
    makeCustomerProcesses(Rest, BankList).
