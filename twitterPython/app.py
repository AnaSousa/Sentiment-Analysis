import sys
import json
from client import *

if len(sys.argv) != 3:
	print("Usage: python <filename> <hashtag> <number_of_results>")
	sys.exit(1)

if not sys.argv[2].isdigit():
	print("Usage: python <filename> <hashtag> <number_of_results>")
	sys.exit(1)

hashtag = sys.argv[1]
number_of_results = sys.argv[2]

# The consumer secret is an example and will not work for real requests
# To register an app visit https://dev.twitter.com/apps/new
CONSUMER_KEY = 'MJ3ty7vpEIiYsVNipROlgJWOw'
CONSUMER_SECRET = '9FMlEdGzqQmXLfxxq1xsmeYclWudrJJb6VE7iI9yqyipngdaZf'

client = Client(CONSUMER_KEY, CONSUMER_SECRET)

# Pretty print of tweet payload
#tweet = client.request('https://api.twitter.com/1.1/statuses/show.json?id=316683059296624640')
#print json.dumps(tweet, sort_keys=True, indent=4, separators=(',', ':'))

#tweets = client.request('https://api.twitter.com/1.1/search/tweets.json?q=%23benfica&result_type=recent&count=10')
#print json.dumps(tweets, sort_keys=True, indent=4, separators=(',', ':'))
tweets = client.getTweetsByHashTag(hashtag, number_of_results)

f = open('statuses.txt', 'w')
 
for t in tweets['statuses']:
	#print t['text'] +'\n'
	f.write(str((t['text'] +'\n').encode('utf8')) + '\n')

print("end of script")



# Show rate limit status for this application
#status = client.rate_limit_status()
#print status['resources']['search']
