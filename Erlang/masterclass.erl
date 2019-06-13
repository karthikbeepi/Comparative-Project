-module(masterclass).
-export([start/0]).

start() ->
        {ok, Banks} = file:consult("banks.txt"),
        {ok ,Customers} = file:consult("customers.txt"),
        io:fwrite("~p : ~p ~n",[Banks, Customers]).