### conda install diagrams
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
with Diagram('prototipo1Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxprototipo1', graph_attr=nodeattr):
          mocktruck=Custom('mocktruck','./qakicons/symActorSmall.png')
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          sonar23=Custom('sonar23','./qakicons/symActorSmall.png')
          sonar=Custom('sonar(coded)','./qakicons/codedQActor.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
          distancefilter=Custom('distancefilter(coded)','./qakicons/codedQActor.png')
     coldstorageservice >> Edge( label='local_movef', **eventedgeattr, fontcolor='red') >> sys
     transporttrolley >> Edge( label='local_movef', **eventedgeattr, fontcolor='red') >> sys
     sonar23 >> Edge( label='alarm', **eventedgeattr, fontcolor='red') >> sys
     coldstorageservice >> Edge(color='magenta', style='solid', decorate='true', label='<pickup &nbsp; >',  fontcolor='magenta') >> transporttrolley
     mocktruck >> Edge(color='magenta', style='solid', decorate='true', label='<storeFood &nbsp; sendTicket &nbsp; deposit &nbsp; >',  fontcolor='magenta') >> coldstorageservice
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<engage &nbsp; moverobot &nbsp; >',  fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid',  label='<disengage &nbsp; setrobotstate &nbsp; cmd &nbsp; >',  fontcolor='blue') >> basicrobot
diag