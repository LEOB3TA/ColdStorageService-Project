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
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxrasp', graph_attr=nodeattr):
          ledqakactor=Custom('ledqakactor','./qakicons/symActorSmall.png')
          controller23=Custom('controller23','./qakicons/symActorSmall.png')
          sonar23=Custom('sonar23(coded)','./qakicons/codedQActor.png')
     with Cluster('ctxstorageservice', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     with Cluster('ctxtruck', graph_attr=nodeattr):
          serviceaccesgui=Custom('serviceaccesgui','./qakicons/symActorSmall.png')
     controller23 >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonar23
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> controller23
     sys >> Edge(color='red', style='dashed', xlabel='robotmoving', fontcolor='red') >> controller23
     controller23 >> Edge(color='blue', style='solid', xlabel='ledCmd', fontcolor='blue') >> ledqakactor
     coldstorageservice >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolley
     coldstorageservice >> Edge(color='blue', style='solid', xlabel='dropout', fontcolor='blue') >> transporttrolley
     coldstorageservice >> Edge(color='blue', style='solid', xlabel='backhome', fontcolor='blue') >> transporttrolley
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='step', fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid', xlabel='cmd', fontcolor='blue') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid', xlabel='end', fontcolor='blue') >> basicrobot
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='doplan', fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid', xlabel='disengage', fontcolor='blue') >> basicrobot
     serviceaccesgui >> Edge(color='magenta', style='solid', xlabel='storeFood', fontcolor='magenta') >> coldstorageservice
diag
