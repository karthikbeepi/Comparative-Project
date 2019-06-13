-module(calling).

-export([process_people/1]).

process_people(Master) ->
	{_, NamePid} = erlang:process_info(self(), registered_name),

	receive
		{Master,  Contacts} ->
			contact(NamePid, Contacts),
			process_people(Master);

		{NamePidFrom, initial, TimeStamp} ->
			Master ! {initial, NamePidFrom, NamePid, TimeStamp},

			NamePidFrom ! {NamePid, reply, TimeStamp},
			
			process_people(Master);

		{NamePidFrom, reply, TimeStamp} ->
			Master ! {reply, NamePidFrom, NamePid, TimeStamp},
			process_people(Master)
	after 
		1000 -> Master ! {NamePid, stop}
	end.

contact(_,[]) -> ok;			
contact(NamePid, [Person | Tail]) ->
	{_, _, TimeStamp} = erlang:now(),
	Person ! {NamePid, initial, TimeStamp},
	contact(NamePid , Tail).

