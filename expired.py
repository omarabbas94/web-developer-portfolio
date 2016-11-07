import elasticsearch
import curator
import datetime as DT


client = elasticsearch.Elasticsearch()

ilo = curator.IndexList(client)
today = DT.date.today()
expiring = today + DT.timedelta(days=2)

ilo.filter_by_regex(kind='suffix', value= expiring )
delete_indices = curator.DeleteIndices(ilo)
delete_indices.do_dry_run()
print ilo.indices

