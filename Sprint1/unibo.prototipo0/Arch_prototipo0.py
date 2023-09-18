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
with Diagram('prototipo0Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxprototipo0', graph_attr=nodeattr):
          mocktruck=Custom('mocktruck','./qakicons/symActorSmall.png')
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     mocktruck >> Edge(color='magenta', style='solid', xlabel='storeFood', fontcolor='magenta') >> coldstorageservice
     mocktruck >> Edge(color='magenta', style='solid', xlabel='sendTicket', fontcolor='magenta') >> coldstorageservice
     mocktruck >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> coldstorageservice
     sys >> Edge(color='red', style='dashed', xlabel='local_movef', fontcolor='red') >> coldstorageservice
     coldstorageservice >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolley
     coldstorageservice >> Edge( xlabel='local_movef', **eventedgeattr, fontcolor='red') >> sys
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid', xlabel='disengage', fontcolor='blue') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid', xlabel='setrobotstate', fontcolor='blue') >> basicrobot
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='moverobot', fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid', xlabel='cmd', fontcolor='blue') >> basicrobot
     transporttrolley >> Edge( xlabel='local_movef', **eventedgeattr, fontcolor='red') >> sys
diag
