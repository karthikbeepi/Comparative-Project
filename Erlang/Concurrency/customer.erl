-module(customer).
-export([makeCustomerProcesses/2, customer/5 , getRandomBank/2]).

getRandomBank(Blist, DontCall) ->
        Total_length = length(Blist),
        BBlist = lists:nth(rand:uniform(Total_length), Blist),
        Flag = lists:member(BBlist, DontCall),
        if
            (Total_length == length(DontCall)) ->
                stop;
            (Flag == true) ->
                getRandomBank(Blist, DontCall);
            true ->
                BBlist
        end
.
customer(CustomerName, LoanAmt, BankList, NotToCall, Acc) when LoanAmt =< 0 ->
    master_recv ! {CustomerName, done, Acc},
    % io:fwrite("Reached!"),
    ok;
customer(CustomerName, LoanAmt, BankList, NotToCall, Acc) when LoanAmt > 0->
    RandomBank = getRandomBank(BankList, NotToCall),
    timer:sleep(rand:uniform(100)+10),
    if
        (RandomBank == stop) ->
            master_recv ! {CustomerName, notdone, Acc},
            ok;
        (RandomBank /= stop) ->
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
                            if
                                (RandomAmt >= LoanAmt) ->
                                    customer(CustomerName, LoanAmt-LoanAmt, BankList, NotToCall, Acc+LoanAmt);
                                (RandomAmt < LoanAmt) ->
                                    customer(CustomerName, LoanAmt-RandomAmt, BankList, NotToCall, Acc+RandomAmt)
                            end;
                    {no} ->
                        % NewList = lists:append(NotToCall, RandomBank),
                        NewList = [RandomBank | NotToCall],
                        % io:fwrite("~p \n", NewList),
                        customer(CustomerName, LoanAmt, BankList, NewList, Acc)
                end
            end
    .

makeCustomerProcesses([], BankList) ->
ok;
makeCustomerProcesses([{K , V}|Rest], BankList) ->
    register(K, spawn(?MODULE, customer, [K, V, BankList, [], 0])),
    makeCustomerProcesses(Rest, BankList).
