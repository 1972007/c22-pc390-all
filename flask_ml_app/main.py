from flask import Flask, request,url_for, render_template, jsonify
import tensorflow as tf
import numpy as np
import os
from PIL import Image
from werkzeug.utils import secure_filename

#Global variables (load model and folder name)
app = Flask(__name__)
model = tf.keras.models.load_model("ham10000_model_v_augment_xception")
temp_img = "/home/m2243f2157/flask_ml_app/temp" #Temporarily save file just in case. Will be deleted after prediction gives result

list_res = ['Actinic keratoses', 
            'basal cell carcinoma ', 
            'benign keratosis-like lesions', 
            'dermatofibroma', 
            'melanoma',
            'melanocytic nevi', 
            'vascular lesions']
#Some index func to start a day
@app.route("/")
def index():
    return render_template("index.html")

#Function to predict what disease is this
@app.route("/predict", methods=['GET','POST'])
def predict():
    response = {
                    'filename' : '',
                    'penyakit' : '',
                    'solusi' : "Please use post"
                }
    if(request.method == 'POST'):
        
        image = request.files.get("image_file")
        if(image):
                
            image_filename = secure_filename(image.filename)
            path = image_filename
            image.save(path)
            response = model_predict(path)
            os.remove(path)
        else:
            response['solusi'] = "Image tidak terkirim"
    return jsonify(response)

def model_predict(img_path):
    image_path = img_path
    image = tf.keras.preprocessing.image.load_img(image_path, target_size=(600, 450))
    input_arr = tf.keras.preprocessing.image.img_to_array(image)
    input_arr = np.array([input_arr])
    results = model.predict(input_arr)
    response =  {
                    'filename' : img_path.split("/")[-1],
                    'penyakit' : list_res[np.argmax(results)],
                    'solusi' : "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                }
    print(response)
    return response
    
if __name__=="__main__":
    app.run(host="127.0.0.1", port=8080)
    #print(model_predict("ISIC_0034321.jpg"))