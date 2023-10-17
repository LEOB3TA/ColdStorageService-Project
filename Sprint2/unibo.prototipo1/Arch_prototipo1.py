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
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          sonar23=Custom('sonar23','./qakicons/symActorSmall.png')
          ledqakactor=Custom('ledqakactor','./qakicons/symActorSmall.png')
          controller23=Custom('controller23','./qakicons/symActorSmall.png')
          sonar=Custom('sonar(coded)','./qakicons/codedQActor.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
          distancefilter=Custom('distancefilter(coded)','./qakicons/codedQActor.png')
     coldstorageservice >> Edge( label='local_movef', **eventedgeattr, fontcolor='red') >> sys
     transporttrolley >> Edge( label='robotathome', **eventedgeattr, fontcolor='red') >> sys
     transporttrolley >> Edge( label='robotmoving', **eventedgeattr, fontcolor='red') >> sys
     transporttrolley >> Edge( label='local_movef', **eventedgeattr, fontcolor='red') >> sys
     sonar23 >> Edge( label='resume', **eventedgeattr, fontcolor='red') >> sys
     sonar23 >> Edge( label='alarm', **eventedgeattr, fontcolor='red') >> sys
     coldstorageservice >> Edge(color='magenta', style='solid', decorate='true', label='<pickup<font color="darkgreen"> pickupdone</font> &nbsp; >',  fontcolor='magenta') >> transporttrolley
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     controller23 >> Edge(color='blue', style='solid',  label='<ledCmd &nbsp; >',  fontcolor='blue') >> ledqakactor
     transporttrolley >> Edge(color='blue', style='solid',  label='<disengage &nbsp; setrobotstate &nbsp; cmd &nbsp; >',  fontcolor='blue') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid',  label='<gotomovetoport &nbsp; gotodepositactionended &nbsp; gotorobottohome &nbsp; >',  fontcolor='blue') >> transporttrolley
     controller23 >> Edge(color='blue', style='solid',  label='<sonaractivate &nbsp; >',  fontcolor='blue') >> sonar23
diag
