% hello world program
-module(helloworld).
-export([simply/1, start/0, fac/1]).

fac(X) when X == 0  -> 1;
fac(X) when X > 0 -> X * fac(X-1). 


simply(X) when X < 3 ->
    io:fwrite("Hi there!").

start() ->
    N = fac(3),
    if 

        6 < 6 -> simply(10);
        true ->  io:fwrite("~w", [N])
end.


