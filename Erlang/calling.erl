-module(exchange).
-import(calling,[handle/2]). 
-export([start/0]).

start() ->
	Initial = file:consult("calls.txt"),
	H=element(2,Initial),
	io:fwrite("** Calls to be made ** ~n" ),
	io:fwrite("~n"),
	Mpp=maps:from_list(H),	
	maps:fold(fun(K, V, ok) -> io:format("~p: ~p~n", [K, V]) end, ok, Mpp), 
	io:fwrite("~n"),
	
	Fun1=fun(K,V,Acc)->
	%timer:sleep(round(timer:seconds(random:uniform()))),
	Pid = spawn(calling, handle, [K,V]),
	Pid ! {self()}
	end,
	maps:fold(Fun1,[],Mpp),
	rec_mesg().

rec_mesg() ->
receive
        {Message} ->
				io:format("~s~n", [Message]),
				rec_mesg()
		after 1500 ->
				io:format("Master has received no replies for 1.5 second, ending...~n", [])
    end.