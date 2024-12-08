from flask import Blueprint, abort, request, render_template, redirect
import requests

router = Blueprint('router', __name__)

@router.route('/')
def home():
    
    r = requests.get('http://localhost:8099/myapp/proyecto/list')
       
    r.raise_for_status()  
    data = r.json() 
    return render_template('index.html', proyectos=data["data"])  # Pasar los datos a la plantilla