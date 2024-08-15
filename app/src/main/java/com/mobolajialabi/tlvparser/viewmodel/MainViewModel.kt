package com.mobolajialabi.tlvparser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobolajialabi.tlvparser.model.Tlv
import com.mobolajialabi.tlvparser.utils.TlvParser
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _tlvList: MutableLiveData<List<Tlv>> = MutableLiveData()
    val tlvList: LiveData<List<Tlv>>
        get() = _tlvList

    private val _errorString: MutableLiveData<String?> = MutableLiveData()
    val errorString: LiveData<String?>
        get() = _errorString

    /**Creates an object of the TlvParser class
     * Runs through the item and parses it into a list of TLV values
     */
    fun parseTlv(tlvData: String) {
        viewModelScope.launch {
            val parser = TlvParser(tlvData)
            val tlvValues = ArrayList<Tlv>()
            var index = 0

            while (index < (tlvData.length / 2 - 1)) {
                val tag = parser.getTag(index)
                index += (tag.length / 2)
                val length = parser.getLength(index)

                //Returns an error if the length is more than 2 bytes as specified in EMVco specifications
                if (length.size > 2) {
                    _errorString.value =
                        "Error parsing tag $tag. Tag length must not be greater then two bytes"
                    break
                }
                index += length.size
                val value = parser.getValue(index, length)
                //the value being null implies the length exceeds beyond the last index of the provided tlv string
                if (value == null) {
                    _errorString.value =
                        "Error parsing tag $tag. Value length exceeds end of inputted string"
                    break
                }
                index += value.length / 2
                tlvValues.add(Tlv(tag, value.length / 2, value))
            }

            _tlvList.value = tlvValues
        }
    }
}