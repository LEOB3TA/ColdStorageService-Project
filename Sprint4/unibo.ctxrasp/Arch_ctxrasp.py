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
with Diagram('ctxraspArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxrasp', graph_attr=nodeattr):
          sonar23=Custom('sonar23','./qakicons/symActorSmall.png')
          ledqakactor=Custom('ledqakactor','./qakicons/symActorSmall.png')
          sonar=Custom('sonar(coded)','./qakicons/codedQActor.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
     sonar23 >> Edge( label='stop', **eventedgeattr, fontcolor='red') >> sys
     sonar23 >> Edge( label='resume', **eventedgeattr, fontcolor='red') >> sys
     ledqakactor >> Edge(color='blue', style='solid',  label='<gotoblink &nbsp; >',  fontcolor='blue') >> ledqakactor
diag
