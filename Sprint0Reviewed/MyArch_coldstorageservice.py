import os
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom

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
  with Cluster('ctxtruck', graph_attr=nodeattr):
     serviceaccesgui=Custom('serviceaccesgui','./qakicons/symActorSmall.png')
  with Cluster('env'):
     # sys = Custom('','./qakicons/system.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxrasp', graph_attr=nodeattr):
          ledqakactor=Custom('led','./qakicons/symActorSmall.png')
          sonar23=Custom('sonar23(coded)','./qakicons/codedQActor.png')
     with Cluster('ctxstorageservice', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          servicestatusgui=Custom('servicestatusgui','./qakicons/symActorSmall.png')
     coldstorageservice >> Edge(color='forestgreen', style='dashed', xlabel='chargetaken', fontcolor='forestgreen') >> serviceaccesgui
     serviceaccesgui >> Edge(color='magenta', style='solid', xlabel='storeFood', fontcolor='magenta') >> coldstorageservice
     serviceaccesgui >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> coldstorageservice
     coldstorageservice >> Edge(color='forestgreen', style='dashed', xlabel='\t store accepted(TICKET) \n store rejected', fontcolor='forestgreen') >> serviceaccesgui
    # coldstorageservice >> Edge(color='forestgreen', style='dashed', xlabel='', fontcolor='forestgreen') >> serviceaccesgui
diag
