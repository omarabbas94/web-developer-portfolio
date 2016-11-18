import elasticsearch
import curator
import datetime

client = elasticsearch.Elasticsearch()

ilo = curator.IndexList(client)
today = datetime.date.today()
ilo.filter_by_regex(kind='suffix', value= today )
delete_indices = curator.DeleteIndices(ilo)
delete_indices.do_dry_run()
print ilo.indices

