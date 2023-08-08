from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('coldstorageserviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxsonarqak23', graph_attr=nodeattr):
          sonar=Custom('sonar(coded)','./qakicons/codedQActor.png')
     with Cluster('ctxledqak', graph_attr=nodeattr):
          ledqakactor=Custom('ledqakactor','./qakicons/symActorSmall.png')
     with Cluster('ctxstorageservice', graph_attr=nodeattr):
          coldstorageserviceactor=Custom('coldstorageserviceactor','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     coldstorageserviceactor >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolley
diag
