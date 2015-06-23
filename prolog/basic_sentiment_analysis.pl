print_sentiment(Value) :-
	Value > 0,
	write('positive').
print_sentiment(Value) :-
	Value < 0,
	write('negative').
print_sentiment(Value) :-
	Value = 0,
	write('neutral').

value_of(positive, 1).
value_of(negative, -1).
value_of(_, 0).

prevTokValue([],CurrentValue,CurrentValue).
prevTokValue([_, inc], CurrentValue, OutputValue) :-
	OutputValue is CurrentValue * 2, !.
prevTokValue([_, dec], CurrentValue, OutputValue) :-
	OutputValue is CurrentValue / 2, !.
prevTokValue([_, inv], CurrentValue, OutputValue) :-
	OutputValue is CurrentValue * -1, !.
prevTokValue([_, _], CurrentValue, CurrentValue):- !.

sentence_score([], _, 0).	
sentence_score([[Word, Tag]|Tail], PrevToken, TotalScore) :- 
	value_of(Tag, ValueTag),
	prevTokValue(PrevToken, ValueTag, CurScore),
	CurToken = [Word, Tag],!,
	sentence_score(Tail, CurToken, TotalofRest),
	TotalScore is CurScore + TotalofRest.

tag_sentence([Word |Reststring], Reststring, [Word, Category]) :-
	dict(Word,Category).
tag_sentence([Word |Reststring], Reststring, [Word, nothing]) :-
	\+(dict(Word,_)).
tag_sentences([],String, String).
tag_sentences(String, Reststring, [Subtree|Subtrees]) :-
	tag_sentence(String, String1, Subtree),
	tag_sentences(String1, Reststring, Subtrees).

sentiment(String,Value) :-
        write(String),write('\n'),
        tag_sentences(String, [], TaggedResult),
        write(TaggedResult),write('\n'),!,
		sentence_score(TaggedResult, [], Value),!,
		%totalScore(Value),
		write(Value),
		write('\nThe sentence is: '), print_sentiment(Value), nl.


%%server

user:runtime_entry(start) :- server.

:- use_module(library(sockets)).


port(2000).

server:-
        port(Port),
        socket_server_open(Port,Socket),
        socket_server_accept(Socket, _Client, Stream, [type(text)]),
        server_loop(Stream),
        socket_server_close(Socket),
        write('Closing'),nl.

server_loop(Stream) :-
                %read_line(Stream, Line),
                %write(Line),
                read(Stream, ClientRequest),
                write('Received: '), write(ClientRequest), nl,
                server_valid(ClientRequest,Stream).

server_valid(end_of_file,_):-!.
server_valid(ClientRequest,Stream) :-
                server_input(ClientRequest, ServerReply),
                format(Stream, '~q.~n', [ServerReply]),
                write('Sent: '), write(ServerReply), nl, 
                flush_output(Stream),server_loop(Stream).

server_input(evaluate_str(Str),Value):- sentiment(Str,Value),!.
