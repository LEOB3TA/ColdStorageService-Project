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
    'style': 'dotted',
    'decorate': 'false'
}
with Diagram('Sprint2Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
    with Cluster('ctxtruck', graph_attr=nodeattr):
        mocktruck=Custom('mocktruck','./qakicons/symActorSmall.png')
    with Cluster('env'):
        sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
        with Cluster('ctxbasicrobot', graph_attr=nodeattr):
            basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
        with Cluster('ctxsonar', graph_attr=nodeattr):
            sonar23=Custom('sonar23','./qakicons/symActorSmall.png')
            sonar=Custom('sonar(coded)','./qakicons/codedQActor.png')
            ledqakactor=Custom('ledqakactor','./qakicons/symActorSmall.png')
            datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
        with Cluster('ctxstorageserviece', graph_attr=nodeattr):
            coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
            transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
            stateobservercontroller=Custom('stateobservercontroller','./qakicons/symActorSmall.png')
    #transporttrolley >> Edge( label='local_movef', **eventedgeattr, fontcolor='red') >> sys
    transporttrolley >> Edge( label='<alarm &nbsp;>', **eventedgeattr, fontcolor='red') >> sys
    ##TAKEN events
    #sys >> Edge( label='alarm', **eventedgeattr, fontcolor='red') >> basicrobot
    #sys >>  Edge( label='resume', **eventedgeattr, fontcolor='red') >> transporttrolley
    #sys >> Edge( label='stop', **eventedgeattr, fontcolor='red') >> transporttrolley
    ##
    sonar >>  Edge(color='blue', style='solid',  fontcolor='blue') >> datacleaner
    datacleaner >> Edge(color='blue', style='solid',  label='<sonardata &nbsp;>',  fontcolor='blue') >> sonar23
    sonar23 >> Edge( label='<resume &nbsp;>', **eventedgeattr, fontcolor='red') >> sys
    sonar23 >> Edge( label='<stop &nbsp;>', **eventedgeattr, fontcolor='red') >> sys
    coldstorageservice >> Edge(color='magenta', style='solid', decorate='false', label='<pickup<font color="darkgreen"> pickupdone</font> &nbsp; >',  fontcolor='magenta') >> transporttrolley
    mocktruck >> Edge(color='magenta', style='solid', decorate='false', label='<storeFood<font color="darkgreen"> storeAccepted storeRejected</font> &nbsp; sendTicket<font color="darkgreen"> ticketValid ticketNotValid ticketExpired</font> &nbsp; deposit<font color="darkgreen"> chargeTaken</font> &nbsp; >',  fontcolor='magenta') >> coldstorageservice
    transporttrolley >> Edge(color='magenta', style='solid', decorate='false', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
    stateobservercontroller >> Edge(color='blue', style='solid',  label='<ledCmd &nbsp;>',  fontcolor='blue') >> ledqakactor
    transporttrolley >> Edge(color='blue', style='solid',  label='<disengage &nbsp; setrobotstate &nbsp; cmd &nbsp; >',  fontcolor='blue') >> basicrobot
    #  transporttrolley >> Edge(color='blue', style='solid',  label='<gotomovetoport &nbsp; gotodepositactionended &nbsp; gotorobottohome &nbsp; >',  fontcolor='blue') >> transporttrolley #commentato perhcè sono messaggi che si autoinvia
diag
