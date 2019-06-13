-module(masterclass).
-export([start/0]).

start() ->
    {ok, Banks} = file:consult("banks.txt")
    .