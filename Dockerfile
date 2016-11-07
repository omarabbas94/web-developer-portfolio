FROM ubuntu

MAINTAINER omar abbas

RUN     apt-get update

RUN     apt-get install python-pip -y  
 	 
RUN	pip install elasticsearch-curator

RUN     pip install datetime 

ADD	delete_indices.py /delete_indices.py

CMD     ["python", "delete_indices.py"]

 
