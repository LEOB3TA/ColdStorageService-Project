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
with Diagram('serviceaccessguiArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxcoldstorageservice', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxserviceaccessgui', graph_attr=nodeattr):
          mocktruck=Custom('mocktruck','./qakicons/symActorSmall.png')
     mocktruck >> Edge(color='magenta', style='solid', decorate='true', label='<storeFood<font color="darkgreen"> storeAccepted storeRejected</font> &nbsp; sendTicket<font color="darkgreen"> ticketValid ticketNotValid ticketExpired</font> &nbsp; deposit<font color="darkgreen"> chargeTaken</font> &nbsp; >',  fontcolor='magenta') >> coldstorageservice
diag
