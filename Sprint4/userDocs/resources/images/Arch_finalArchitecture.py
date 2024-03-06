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
with Diagram('sprint4arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  sys = Custom('','./qakicons/system.png')
  with Cluster('ExternalDevice'):
      serviceAccessGUI=Custom('ServiceStatusGUI','./qakicons/gui-overlay.png')
  with Cluster('Raspberry'):
      with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
      with Cluster('ctxrasp', graph_attr=nodeattr):
          sonar23=Custom('sonar23','./qakicons/symActorSmall.png')
          ledqakactor=Custom('ledqakactor','./qakicons/symActorSmall.png')
          sonar=Custom('sonar(coded)','./qakicons/codedQActor.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
          sonar23 >> Edge( label='stop', **eventedgeattr, fontcolor='red') >> sys
          sonar23 >> Edge( label='resume', **eventedgeattr, fontcolor='red') >> sys
  with Cluster('ColdStorageServiceDevice'):
      server=Custom('server','./qakicons/serverLogo1.png')
      serviceStatusGUI=Custom('ServiceStatusGUI','./qakicons/gui-overlay.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
      with Cluster('ctxcoldstorageservice', graph_attr=nodeattr):
          guicontroller=Custom('guicontroller','./qakicons/symActorSmall.png')
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          stateobservercontroller=Custom('stateobservercontroller','./qakicons/symActorSmall.png')
          transporttrolley >> Edge( label='alarm', **eventedgeattr, fontcolor='red') >> sys
          transporttrolley >> Edge( label='local_movef', **eventedgeattr, fontcolor='red') >> sys
          coldstorageservice >> Edge(color='magenta', style='solid', decorate='false', label='<pickup<font color="darkgreen"> pickupdone</font> &nbsp; >',  fontcolor='magenta') >> transporttrolley
          transporttrolley >> Edge(color='magenta', style='solid', decorate='false', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
          stateobservercontroller >> Edge(color='blue', style='solid',  label='<ledCmd &nbsp; >',  fontcolor='blue') >> ledqakactor
          transporttrolley >> Edge(color='blue', style='solid',  label='<disengage &nbsp; setrobotstate &nbsp; cmd &nbsp; >',  fontcolor='blue') >> basicrobot
          transporttrolley >> Edge(color='blue', style='solid',  label='<gotomovetoport &nbsp; gotodepositactionended &nbsp; gotorobottohome &nbsp; >',  fontcolor='blue') >> transporttrolley
          coldstorageservice >> Edge(color='blue', style='solid',  label='<updateS &nbsp; updateR &nbsp; >',  fontcolor='blue') >> guicontroller
          transporttrolley >> Edge(color='blue', style='solid',  label='<coapUpdate &nbsp; >',  fontcolor='blue') >> guicontroller
          transporttrolley >> Edge(color='blue', style='solid',  label='<coapUpdate &nbsp; >',  fontcolor='blue') >> stateobservercontroller
          server >> Edge(color='blue', style='solid',  label='<update &nbsp; >',  fontcolor='blue') >> serviceStatusGUI
          server >> Edge(color='blue', style='solid',  label='<update &nbsp; >',  fontcolor='blue') >> serviceAccessGUI
          server >> Edge(color='magenta', style='solid', decorate='false', label='<request<font color="darkgreen"> reply</font> &nbsp; >',  fontcolor='magenta') >> serviceAccessGUI
          server >> Edge(color='magenta', style='solid', decorate='false', label='<client request<font color="darkgreen"> reply to client</font> &nbsp; >',  fontcolor='magenta') >> guicontroller
          datacleaner >> Edge(color='blue', style='solid',  label='<sonar data &nbsp; >',  fontcolor='blue') >> sonar23
          sonar >> Edge(color='blue', style='solid',  label='<sonar distance &nbsp; >',  fontcolor='blue') >> datacleaner
diag
