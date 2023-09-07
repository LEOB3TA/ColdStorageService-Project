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
     with Cluster('ctxcoldstorageservice', graph_attr=nodeattr):
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
     with Cluster('ctxtruck', graph_attr=nodeattr):
          mocktruck=Custom('mocktruck(ext)','./qakicons/externalQActor.png')
     coldstorageservice >> Edge(color='blue', style='solid', xlabel='dropout', fontcolor='blue') >> transporttrolley
     coldstorageservice >> Edge(color='blue', style='solid', xlabel='gotohome', fontcolor='blue') >> transporttrolley
     coldstorageservice >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolley
diag
