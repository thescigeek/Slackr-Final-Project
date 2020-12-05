package com.example.slackr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class CreateGroupFragment : DialogFragment() {

    var groupNameET: EditText? = null
    var groupDescriptionET:EditText? = null
    var groupLogisiticsET:EditText? = null
    var groupParticipantET:EditText? = null
    var closeTV : TextView?= null
    var createbtn: Button? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var databaseGroups: DatabaseReference


    companion object {

        fun newInstance(subject:String, code:String): DialogFragment {
            val fragment = CreateGroupFragment()
            val bundle = Bundle()
            bundle.putString("subject", subject)// set msg here,
            bundle.putString("code",code)
            fragment.arguments=bundle
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var createView=inflater.inflate(R.layout.fragment_create_group, container, false)
        dialog?.setCanceledOnTouchOutside(false)
        createbtn= createView.findViewById(R.id.create_group_btn)
        groupNameET = createView.findViewById(R.id.groupName)
        groupDescriptionET = createView.findViewById(R.id.group_descrition)
        groupLogisiticsET = createView.findViewById(R.id.group_logistics)
        groupParticipantET = createView.findViewById(R.id.group_participant)
        closeTV = createView.findViewById(R.id.close_create)
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        databaseGroups = mDatabase!!.reference.child("Groups")

        closeTV!!.setOnClickListener{
            dismiss()
        }
        createbtn!!.setOnClickListener{
            val subject =arguments!!.get("subject") as String
            val code= arguments!!.get("code") as String
            var group = StudyGroup(groupNameET!!.text.toString(), groupDescriptionET!!.text.toString(), groupLogisiticsET!!.text.toString(),
                groupParticipantET!!.text.toString(),
                subject, code, mAuth!!.currentUser!!.uid

            )
            val id = (databaseGroups.push()).key.toString()
            databaseGroups.child(id).setValue(group)

                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Group created!", Toast.LENGTH_LONG).show()

                } else{
                    Toast.makeText(activity, "Unable to create the group. Please try again later!", Toast.LENGTH_LONG).show()
                }
                    activity!!.supportFragmentManager.popBackStack()
            }
            val uid = mAuth!!.currentUser?.uid

            uid?.let { mDatabaseReference!!.child(it)}?.addListenerForSingleValueEvent(object :
                ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {
                    val learningStyle = snapshot.child("style_of_learning").value as String
                    databaseGroups.child(id).child("groupLearningStyle").setValue(learningStyle)
                    var key: String =subject+code+learningStyle
                    databaseGroups.child(id).child("searchKey").setValue(key)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }


            )

            activity!!.finish()
        }


        return createView
    }


}