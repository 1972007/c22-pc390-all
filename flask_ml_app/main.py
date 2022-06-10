from flask import Flask, url_for, request, render_template, jsonify
import tensorflow as tf
import numpy as np
import os
from PIL import Image

app = Flask(__name__)
model = tf.keras.models.load_model("ham10000_model_v_augment_xception")
temp_img = "temp"


def model_predict(list_result, img_path="ISIC_0034321.jpg"):
    image_path = img_path
    image = tf.keras.preprocessing.image.load_img(image_path, target_size=(600, 450))
    input_arr = tf.keras.preprocessing.image.img_to_array(image)
    input_arr = np.array([input_arr])
    results = model.predict(input_arr)
    response =  {
                    'filename' : "kulit.png",
                    'penyakit' : list_result[np.argmax(results)],
                    'solusi' : "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                }
    return response


@app.route("/")
def index():
    return render_template("index.html")

@app.route("/", methods=['POST'])
def predict():
    list_res = ['Actinic keratoses', 'basal cell carcinoma ', 'benign keratosis-like lesions', 'dermatofibroma', 'melanoma', 'melanocytic nevi', 'vascular lesions']
    if(request.method == 'POST'):
        image = request.files.get('imagefile', '')
        path = os.path.join(temp_img, image.filename)
        image.save(path)
        response = model_predict(list_res, img_path=path)
        os.remove(path)
    return jsonify(response)

if __name__=="__main__":
    app.run(host="127.0.0.1", port=8080, debug=True)
#    print(model_predict(
#        ['Actinic keratoses', 
#        'basal cell carcinoma ', 
#        'benign keratosis-like lesions', 
#        'dermatofibroma', 
#        'melanoma', 
#        'melanocytic nevi', 
#        'vascular lesions']))