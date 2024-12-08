from flask import Blueprint, abort, request, render_template, redirect
import requests

proyecto = Blueprint('proyecto', __name__)

@proyecto.route('/')
def home():
    
    r = requests.get('http://localhost:8099/myapp/proyecto/list')
       
    r.raise_for_status()  
    data = r.json() 
    return render_template('index.html', proyectos=data["data"])
@proyecto.route('/list/search/nombre/<value>')
def searh_item(value):
    r = requests.get(f'http://localhost:8099/myapp/proyecto/list/search/nombre/{value}')
    r.raise_for_status()  
    data = r.json()
    proyectos = data.get("data", [])  
    
    if not isinstance(proyectos, list):
        proyectos = [proyectos]
    return render_template('index.html', proyectos = proyectos)

@proyecto.route('/list/search/<atribute>/<value>')
def filter_list(atribute, value):
    r = requests.get(f'http://localhost:8099/myapp/proyecto/list/search/{atribute}/{value}')
    r.raise_for_status()  
    data = r.json()

    return render_template('index.html', proyectos=data["data"])
@proyecto.route('/list/sort/<atribute>/<type>/<opcion>')
def order_list(atribute, type, opcion):
    r = requests.get(f'http://localhost:8099/myapp/proyecto/list/order/{atribute}/{type}/{opcion}')
    r.raise_for_status()  
    data = r.json()

    return render_template('index.html', proyectos=data["data"])
@proyecto.route('/save',methods = ['GET'] )
def form_create():
    return render_template('crear_proyecto.html')

@proyecto.route('/save', methods=['POST'])
def save_project():
    data = {
        "nombre": request.form.get("nombre"),
        "inversion": request.form.get("inversion"),
        "fechaInicio": request.form.get("fechaInicio"),
        "fechaFin": request.form.get("fechaFin"),
        "tiempoDeVida": request.form.get("tiempoDeVida"),
        "capacidad": request.form.get("capacidad")
    }
    r = requests.post('http://localhost:8099/myapp/proyecto/save', json=data)
    r.raise_for_status()
    return redirect('/proyecto/')

@proyecto.route('/edit/<int:id>')
def form_edit(id):
    r = requests.get(f'http://localhost:8099/myapp/proyecto/get/{id}')
    r.raise_for_status()
    proyecto = r.json()["data"]
    return render_template('proyecto_edit.html', proyecto=proyecto)

@proyecto.route('/edit/<int:id>', methods=['POST'])
def update_project(id):
    data = request.form.to_dict()
    data['id'] = id
    r = requests.post('http://localhost:8099/myapp/proyecto/update', json=data)
    r.raise_for_status()
    return redirect('/proyecto/')

@proyecto.route('/delete/<int:id>', methods=['POST'])
def delete_project(id):
    r = requests.delete(f'http://localhost:8099/myapp/proyecto/{id}/delete')
    r.raise_for_status()
    return redirect('/proyecto/')