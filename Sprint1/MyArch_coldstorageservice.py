import os
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom

os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '18',
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
  with Cluster('ctxtruck                            ', graph_attr=nodeattr):
     mockTruck=Custom('mockTruck', './qakicons/symActorSmall.png')
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxstorageservice', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')

     mockTruck >> Edge(color='magenta', style='solid', xlabel='storeFood', fontcolor='magenta') >> coldstorageservice
     mockTruck >> Edge(color='magenta', style='solid', xlabel='sendTicket', fontcolor='magenta') >> coldstorageservice
     mockTruck >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> coldstorageservice
     coldstorageservice >> Edge(color='forestgreen', style='dashed', xlabel='store Reply', fontcolor='forestgreen') >> mockTruck
     coldstorageservice >> Edge(color='forestgreen', style='dashed', xlabel='ticket Reply', fontcolor='forestgreen') >> mockTruck
     coldstorageservice >> Edge(color='forestgreen', style='dashed', xlabel='chargeTaken', fontcolor='forestgreen') >> mockTruck
     transporttrolley >> Edge(color='forestgreen', style='dashed', xlabel='pickupdone', fontcolor='forestgreen') >> coldstorageservice
     coldstorageservice >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolley
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='doplan', fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='moverobot', fontcolor='magenta') >> basicrobot
diag
#engagedone/refuse, doplan