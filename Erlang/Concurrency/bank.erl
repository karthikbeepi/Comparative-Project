-module(bank).
-export([makeBankProcesses/1, bank/2]).


bank(BankName, Balance) ->
    receive
            {Customer , LoanAmt} ->
                if
                    (LoanAmt =< Balance ) ->
                        Customer ! {yes},
                        master_recv ! {Customer, yes, LoanAmt, BankName, Balance-LoanAmt},
                        bank(BankName, Balance-LoanAmt);
                    true ->
                        Customer ! {no},
                        master_recv ! {Customer, no, LoanAmt, BankName, Balance},
                        bank(BankName, Balance)
                end
    after 400 -> master_recv ! {BankName , closing , Balance}
    end
.

makeBankProcesses([]) ->
    ok;
makeBankProcesses([{K , V}|Rest]) ->
    register(K, spawn(?MODULE, bank, [K, V])),
    makeBankProcesses(Rest).