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
     with Cluster('ctxtruck', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          mocktruck=Custom('mocktruck','./qakicons/symActorSmall.png')
     mocktruck >> Edge(color='magenta', style='solid', xlabel='storeFood', fontcolor='magenta') >> coldstorageservice
     mocktruck >> Edge(color='magenta', style='solid', xlabel='sendTicket', fontcolor='magenta') >> coldstorageservice
     mocktruck >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> coldstorageservice
diag
