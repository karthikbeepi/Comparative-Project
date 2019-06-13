-module(exchange).
-export([main/1, master_process/1]).

main(FileName) ->
	{ok, Result} = file:consult(FileName),
 	master_process(Result).
	

master_process(Result) ->
	ListPerson = get_key(Result, []),
	createpid(ListPerson),
        io:format("** Calls to be made **\n", []),
	show(ListPerson, Result),
	send(ListPerson, Result),
	master().


  
master() ->
	receive
		{initial, From, To, TimeStamp} ->
			io:format("~p received intro message from ~p [~p]\n", [ To, From, TimeStamp]), 
			master();
		{reply, From, To, TimeStamp} ->
			io:format("~p received reply message from ~p [~p]\n", [ To, From, TimeStamp]), 
			master();
		{Pid, stop} ->
			io:format("Process ~p has received no calls for 1 second, ending...\n", [Pid]), 
			master()
	after 
		1500 -> io:format("Master has received no replies for 1.5 seconds, ending...\n", [])
	end.



createpid([A | Tail]) ->
	Pid = spawn(calling, process_people, [self()]),
	register(A, Pid),
	createpid(Tail) ;
createpid([]) -> ok.


send([], _Result) -> ok;
send([A| Tail], Result) ->
	A ! {self(), proplists:get_value(A, Result, []) },
	send(Tail, Result).


show([], _) -> io:format("\n", []);
show([Key| Tail], Result) ->
	io:format("~p: ~p\n", [Key, proplists:get_value(Key, Result, [])]),
	show(Tail, Result).

	  
get_key([], Result) -> Result;
get_key([{Key, _}| Tail], R) ->
	get_key(Tail, R ++ [Key]).


