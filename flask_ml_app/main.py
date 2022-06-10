from flask import Flask, url_for, request, render_template, jsonify
import tensorflow as tf
import numpy as np
import os
from PIL import Image

#Global variables (load model and folder name)
app = Flask(__name__)
model = tf.keras.models.load_model("flask_ml_app/ham10000_model_v_augment_xception")
temp_img = "temp" #Temporarily save file just in case. Will be deleted after prediction gives result

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
def predict_img():
    print(request.url)
    response = {
                    'filename' : '',
                    'penyakit' : '',
                    'solusi' : "Please use post"
                }
    if(request.method == 'POST'):
        
        image = request.files.get("imagefile",None)
        path = os.path.join(temp_img, image.filename)
        image.save(path)
        response = model_predict(path)
        os.remove(path)
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
    return response
    
if __name__=="__main__":
    app.run(host="0.0.0.0", port=8080, debug=True)
    #print(model_predict("ISIC_0034321.jpg"))